package org.krose;

import com.krose.display.*;
import com.krose.io.Input;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class NodeReader {
    public static NodeManager createNodeManager(String filename) throws FileNotFoundException {
        NodeManager result = new NodeManager();
        Input in = new Input(new InputStreamReader(NodeReader.class.getResourceAsStream(filename)));
        String s = null;
        NodeContainer container = null;
        while ((s = in.getString()) != null) {
            s = s.trim();
            if (s.isEmpty() || s.charAt(0) == '#') continue;
            String[] s1 = s.split(":");
            String[] s2 = s1[1].trim().split(",");
            String id = s1[0].trim().toLowerCase();
            String type = s2[0].trim().toLowerCase();
            if (type.equals("container")){
                if (container != null) result.addNodeContainer(container);
                container = new NodeContainer(id);
            } else if (container != null) {
                Node node = createNode(id, s2);
                if (node != null) container.addNode(node);
            }
        }
        if (container != null) result.addNodeContainer(container);
        return result;
    }

    private static Node createNode(String id, String[] args) {
        Node result = null;
        String type = args[0].trim().toLowerCase();
        if (type.equals("menu")) {
            String title = args[1].trim();
            String[] options = new String[args.length - 2];
            for (int i = 0; i < options.length; i++) options[i] = args[i + 2];
            result = new MenuNode(id, title, options);
        } else if (type.equals("field")) {
            String fieldType = args[1].trim().toLowerCase();
            String label = args[2].trim();
            if (fieldType.equals("int") || fieldType.equals("integer")) result = new IntegerFieldNode(id, label);
            else if (fieldType.equals("double")) result = new DoubleFieldNode(id, label);
            else if (fieldType.equals("string")) result = new StringFieldNode(id, label);
        }

        return result;
    }
}
