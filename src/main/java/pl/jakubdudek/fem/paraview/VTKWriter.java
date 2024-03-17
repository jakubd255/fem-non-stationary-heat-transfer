package pl.jakubdudek.fem.paraview;

import lombok.AllArgsConstructor;
import pl.jakubdudek.fem.App;
import pl.jakubdudek.fem.globaldata.Element;
import pl.jakubdudek.fem.globaldata.GlobalData;
import pl.jakubdudek.fem.globaldata.Grid;
import pl.jakubdudek.fem.globaldata.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@AllArgsConstructor
public class VTKWriter {
    private GlobalData globalData;

    public void write(double[] temp, int time) throws IOException {
        Grid grid = globalData.getGrid();

        File file = new File("vtk/grid"+(App.FILE_INDEX+1)+"/time"+time+".vtk");
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());

        fileWriter.write("# vtk DataFile Version 2.0\n");
        fileWriter.write("Unstructured Grid Example\n");
        fileWriter.write("ASCII\n");
        fileWriter.write("DATASET UNSTRUCTURED_GRID\n");

        fileWriter.write("\n");

        fileWriter.write("POINTS "+grid.getNodes().length+" float\n");
        for(Node node: grid.getNodes()) {
            fileWriter.write(node.getX()+" "+node.getY()+" 0\n");
        }

        fileWriter.write("\n");
        fileWriter.write("CELLS "+grid.getElements().length+" "+5*grid.getElements().length+"\n");
        for(Element element: grid.getElements()) {
            int[] id = element.getNodes();
            fileWriter.write("4 "+id[0]+" "+id[1]+" "+id[2]+" "+id[3]+"\n");
        }

        fileWriter.write("\n");

        fileWriter.write("CELL_TYPES "+grid.getElements().length+"\n");
        for(Element element: grid.getElements()) {
            fileWriter.write("9\n");
        }

        fileWriter.write("\n");

        fileWriter.write("POINT_DATA "+temp.length+"\n");
        fileWriter.write("SCALARS Temp float 1\n");
        fileWriter.write("LOOKUP_TABLE default\n");
        for(double t: temp) {
            fileWriter.write(t+"\n");
        }

        fileWriter.close();
    }
}
