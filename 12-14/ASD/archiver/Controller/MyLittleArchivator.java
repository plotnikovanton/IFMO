package ru.ifmo.enf.plotnikov.t04_lang.Controller;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MyLittleArchivator{ //Zip archiving is magic
    //Params
    private int bufferSize = 1024;
    private JProgressBar bar = null;
    private int compressionLevel = 2;
    private boolean verbose = false;
    //Package local for tests
    public ZipFolder root = new ZipFolder(null); //Root folder, everything starts here

    public void setBar(JProgressBar bar) {
        this.bar = bar;
    }

    public MyLittleArchivator() {}

    public MyLittleArchivator(JProgressBar bar) {
        this.bar = bar;
    }

    /**
     * Set new buffer size
     *
     * @param bufferSize new size of buffer
     * @return true if all is ok
     */
    public boolean setBufferSize(int bufferSize) {
        if (bufferSize > 1) {
            this.bufferSize = bufferSize;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set new compression level (2 by default)
     *
     * @param compressionLevel new compression level
     * @return true if all is ok
     */
    public boolean setCompressionLevel(int compressionLevel) {
        if (compressionLevel >= 0 && compressionLevel <= 9) {
            this.compressionLevel = compressionLevel;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set verbose output
     * @param verbose
     */
    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }

    /**
     * Create new zip file
     *
     * @param outputFileName output file full name
     */
    public void pack(String outputFileName) throws IOException, InterruptedException {
        //init progressbar
        if (bar != null) {
            bar.setMaximum(count(root));
            bar.setMinimum(0);
            bar.setValue(0);
        }

        FileOutputStream fout = new FileOutputStream(outputFileName);
        ZipOutputStream zout = new ZipOutputStream(fout);
        zout.setLevel(compressionLevel);
        //Lets add all to ZOUT
        addToZout(root, null, zout);
        zout.close();

    }

    private int count(ZipFolder f) {
        int counter = 0;
        counter += f.entries.size();
        for (ZipFolder folder : f.folders) {
            counter += count(folder);
        }
        return counter;
    }

    /**
     *
     * @return  root ZipFolder
     */
    public ZipFolder getRoot() {
        return root;
    }

    /**
     * Collect all data
     *
     * @param folder  folder with new entries
     * @param prevDir path of parent of current dir, should end with "/"
     * @param zout    ZipOutputStream
     */
    private void addToZout(ZipFolder folder, String prevDir, ZipOutputStream zout) throws IOException, InterruptedException {
        byte[] buffer = new byte[bufferSize];
        String newPath = "";
        if (prevDir != null) {
            newPath = new StringBuilder(prevDir).append(folder).append('/').toString(); //Super fast native method
        }
        if (folder.entries.size() == 0 && folder.folders.size() == 0) { //If folder is empty
            if (verbose) System.out.print("Zipping empty folder  "+newPath+" ... ");
            zout.putNextEntry(new ZipEntry(newPath));
            zout.closeEntry();
            if (verbose) System.out.println("done.");
        } else {
            for (File file : folder.entries) {
                FileInputStream fin = new FileInputStream(file);
                zout.putNextEntry(new ZipEntry(newPath + file.getName()));
                int count;
                if (verbose) System.out.print("Zipping   " + file.getPath() + " ... ");
                while ((count = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, count);
                }
                fin.close(); //FIN!
                if (verbose) System.out.println("done.");

                if (bar!=null) bar.setValue(bar.getValue()+1);
                //new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "progress");
                //new ChangeEvent(this);
                //System.out.println("1");
                //Thread.sleep(1000);
            }
            for (ZipFolder f : folder.folders) {
                addToZout(f, newPath, zout);
            }
        }
    }

    /**
     * Class of folder inside zip
     */
    static class ZipFolder {
        String name;
        List<ZipFolder> folders = new ArrayList<>();
        List<File> entries = new ArrayList<>();

        /**
         * @param name name of folder
         */
        ZipFolder(String name) {
            this.name = name;
        }

        void add(ZipFolder folder) {
            folders.add(folder);
        }

        void add(File entry) {
            entries.add(entry);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
