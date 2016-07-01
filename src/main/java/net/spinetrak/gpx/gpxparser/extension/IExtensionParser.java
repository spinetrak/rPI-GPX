package net.spinetrak.gpx.gpxparser.extension;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface IExtensionParser
{

  String getId();

  Object parseExtensions(Node node);

  void writeExtensions(Node node, Document doc);

}
