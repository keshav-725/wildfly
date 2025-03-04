/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.clustering.jgroups.subsystem;

import org.jboss.as.clustering.controller.SubsystemExtension;
import org.jboss.as.clustering.jgroups.LogFactory;
import org.jboss.as.controller.Extension;
import org.jboss.as.controller.descriptions.ParentResourceDescriptionResolver;
import org.jboss.as.controller.descriptions.SubsystemResourceDescriptionResolver;
import org.kohsuke.MetaInfServices;

/**
 * Registers the JGroups subsystem.
 *
 * @author Paul Ferraro
 * @author Richard Achmatowicz (c) 2011 Red Hat Inc.
 */
@MetaInfServices(Extension.class)
public class JGroupsExtension extends SubsystemExtension<JGroupsSchema> {

    static final String SUBSYSTEM_NAME = "jgroups";
    static final ParentResourceDescriptionResolver SUBSYSTEM_RESOLVER = new SubsystemResourceDescriptionResolver(SUBSYSTEM_NAME, JGroupsExtension.class);

    public JGroupsExtension() {
        super(SUBSYSTEM_NAME, JGroupsSubsystemModel.CURRENT, JGroupsSubsystemResourceDefinition::new, JGroupsSchema.CURRENT, new JGroupsSubsystemXMLWriter());

        // Workaround for JGRP-1475
        // Configure JGroups to use jboss-logging.
        org.jgroups.logging.LogFactory.setCustomLogFactory(new LogFactory());
    }
}
