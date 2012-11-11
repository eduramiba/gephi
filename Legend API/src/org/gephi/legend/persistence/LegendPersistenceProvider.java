/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.persistence;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.gephi.legend.api.LegendController;
import org.gephi.project.api.Workspace;
import org.gephi.project.spi.WorkspacePersistenceProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author edubecks
 */
@ServiceProvider(service = WorkspacePersistenceProvider.class)
public class LegendPersistenceProvider implements WorkspacePersistenceProvider{

    @Override
    public void writeXML(XMLStreamWriter writer, Workspace workspace) {
        LegendController.getInstance().writeXML(writer, workspace);
    }

    @Override
    public void readXML(XMLStreamReader reader, Workspace workspace) {
        LegendController.getInstance().readXMLToLegendModel(reader, workspace);
    }

    @Override
    public String getIdentifier() {
        return LegendController.XML_LEGENDS;
    }
    
}
