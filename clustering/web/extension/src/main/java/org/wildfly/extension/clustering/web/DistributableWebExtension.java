/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2018, Red Hat, Inc., and individual contributors
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

package org.wildfly.extension.clustering.web;

import org.jboss.as.clustering.controller.PersistentSubsystemExtension;
import org.jboss.as.controller.Extension;
import org.jboss.as.controller.descriptions.ParentResourceDescriptionResolver;
import org.jboss.as.controller.descriptions.SubsystemResourceDescriptionResolver;
import org.kohsuke.MetaInfServices;

/**
 * Extension that registers the distributable-web subsystem.
 * @author Paul Ferraro
 */
@MetaInfServices(Extension.class)
public class DistributableWebExtension extends PersistentSubsystemExtension<DistributableWebSchema> {

    static final String SUBSYSTEM_NAME = "distributable-web";
    static final ParentResourceDescriptionResolver SUBSYSTEM_RESOLVER = new SubsystemResourceDescriptionResolver(SUBSYSTEM_NAME, DistributableWebExtension.class);

    public DistributableWebExtension() {
        super(SUBSYSTEM_NAME, DistributableWebSubsystemModel.CURRENT, DistributableWebResourceDefinition::new, DistributableWebSchema.VERSION_3_0);
    }
}
