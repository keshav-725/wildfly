/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2022, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.clustering.controller;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.SubsystemModel;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;

/**
 * Generic extension implementation that registers a single subsystem.
 * @author Paul Ferraro
 */
public class SubsystemExtension<S extends Enum<S> & SubsystemSchema<S>> implements Extension {

    private final String name;
    private final SubsystemModel currentModel;
    private final Supplier<ManagementRegistrar<SubsystemRegistration>> registrarFactory;
    private final S currentSchema;
    private final XMLElementWriter<SubsystemMarshallingContext> writer;
    private final Supplier<XMLElementReader<List<ModelNode>>> currentReaderFactory;

    /**
     * Constructs a new extension using a reader factory and a separate writer implementation.
     * @param name the subsystem name
     * @param currentModel the current model
     * @param registrarFactory a factory for creating the subsystem resource definition registrar
     * @param currentSchema the current schema
     * @param readerFactory a factory for creating an XML reader
     * @param writer an XML writer
     */
    protected SubsystemExtension(String name, SubsystemModel currentModel, Supplier<ManagementRegistrar<SubsystemRegistration>> registrarFactory, S currentSchema, XMLElementWriter<SubsystemMarshallingContext> writer) {
        this(name, currentModel, registrarFactory, currentSchema, writer, currentSchema);
    }

    SubsystemExtension(String name, SubsystemModel currentModel, Supplier<ManagementRegistrar<SubsystemRegistration>> registrarFactory, S currentSchema, XMLElementWriter<SubsystemMarshallingContext> writer, Supplier<XMLElementReader<List<ModelNode>>> currentReaderFactory) {
        this.name = name;
        this.currentModel = currentModel;
        this.registrarFactory = registrarFactory;
        this.currentSchema = currentSchema;
        this.writer = writer;
        this.currentReaderFactory = currentReaderFactory;
    }

    @Override
    public void initialize(ExtensionContext context) {
        SubsystemRegistration registration = new ContextualSubsystemRegistration(context.registerSubsystem(this.name, this.currentModel.getVersion()), context);
        // Construct subsystem resource definition here so that instance can be garbage collected following registration
        this.registrarFactory.get().register(registration);
        registration.registerXMLElementWriter(this.writer);
    }

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        for (S schema : EnumSet.allOf(this.currentSchema.getDeclaringClass())) {
            // Register via supplier so that reader is created lazily, and garbage collected when complete
            context.setSubsystemXmlMapping(this.name, schema.getUri(), (schema == this.currentSchema) ? this.currentReaderFactory : schema);
        }
    }
}
