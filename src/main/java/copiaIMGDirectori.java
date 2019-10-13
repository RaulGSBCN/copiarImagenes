import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class copiaIMGDirectori {
    public static void main(String[] args) {
            if (args.length == 2) {
                int contador=0;
                Path dir = Paths.get(args[0]);
                Path ficheroSalida = Paths.get(args[1]);
                if(!Files.exists(ficheroSalida)){
                    try {
                        Files.createDirectory(ficheroSalida);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (Files.isDirectory(dir)) {
                    buscaFicheros(dir, ficheroSalida, contador);
                } else {
                    System.err.println("Ús: java LlistarDirectori <directori>");
                }
            }

        }


    private static void buscaFicheros(Path dir, Path ficheroSalida, int contador) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path fitxer : stream) {
                String []fitxerTemp=fitxer.getFileName().toString().split("\\.");
                String extension=fitxerTemp[fitxerTemp.length-1];
                if(extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg")){
                    contador++;
                    leeGraba(fitxer,ficheroSalida, contador);
                }
                if (Files.isDirectory(fitxer)) {
                    buscaFicheros(fitxer, ficheroSalida, contador);
                }
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
    }

    public static void leeGraba(Path fitxer, Path ficheroSalida,int contador){
        try {
            final int BLOCK_LENGTH = 1000;
            FileInputStream fi = new FileInputStream(fitxer.toAbsolutePath().toString());
            String nombreArchivo=fitxer.getFileName().toString();
            FileOutputStream fo = new FileOutputStream(ficheroSalida.toAbsolutePath().toString()+"/"+contador+"."+nombreArchivo);
            System.out.println("Archivo clonado con éxito: "+ficheroSalida.toAbsolutePath().toString()+"/"+contador+"."+nombreArchivo);

            byte[] blockBytes = new byte[BLOCK_LENGTH];

            int character = fi.read();
            int i=0;
            while (character!=-1) {
                if (i < BLOCK_LENGTH) {
                    blockBytes[i] = (byte) character;

                    character = fi.read();
                    i++;
                } else {
                    fo.write(blockBytes);
                    i = 0;
                }
            }
            if (i>0) {
                fo.write(blockBytes,0,i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
