package il.co.ilrd.BashTree;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Matan Keler
 */
public class Tree {
    private final DirFile root;

    /**
     * @throws InvalidPathException if path not valid
     */
    public Tree (String path) throws InvalidPathException{
        java.io.File f = new java.io.File(path);
        if (!f.exists()) {
            throw new InvalidPathException(path, "path not valid");
        }

        root = new DirFile(0, f.getName());
        root.lst.add(new DirFile(0, path));
        root.CreateTree(path);
        root.SortAll();
    }

    /**
     * print the tree of files accourding to the path given in constructor
     */
    public void print() {
        for (File f : root.lst) {
            f.print();
        }
    }

    private interface File {
        public void print();
        public String getName();
    }

    private class DirFile implements File {
        private final int depth;
        private final String name;
        private final List<File> lst = new ArrayList<File>();

        private DirFile(int depth, String name) {
            this.depth = depth;
            this.name = name;
        }

        private void CreateTree(String path) {
            java.io.File[] f = new java.io.File(path).listFiles();
            if (null == f) {
                return;
            }

            for (java.io.File file: f) {
                if (file.isDirectory() && !file.isHidden()) {
                    DirFile df = new DirFile(depth + 1, file.getName());
                    lst.add(df);
                    df.CreateTree(file.toString());
                }
                else if (!file.isHidden()) {
                    lst.add(new RegularFile(depth + 1, file.getName()));
                }
            }
        }

        private void SortAll() {
            lst.sort((a,b) -> {
                return (a.getName().compareTo(b.getName()));
            });

            for (File f : lst) {
                if (f instanceof DirFile) {
                    ((DirFile)f).lst.sort((a,b) -> {
                        return (a.getName().compareTo(b.getName()));
                    });
                }
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void print() {
            for (int i = 0; i < depth; ++i) {
                System.out.print("\033[0m" + "  ");
            }

            System.out.println("|_" + "\033[1;34m" + name);
            for (File f: lst) {
                f.print();
            }
        }
    }

    private class RegularFile implements File {
        private final int depth;
        private final String name;

        private RegularFile(int depth, String name) {
            this.name = name;
            this.depth = depth;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void print() {
            for (int i = 0; i < depth; ++i) {
                System.out.print("\033[0m" + "  ");
            }

            System.out.println("\033[0m" + "|_" + name);
        }
    }

    public static void main(String[] args) {
        if (0 == args.length) {
            Tree t = new Tree(".");
            t.print();
        }
        else {
            Tree t = new Tree(args[0]);
            t.print();
        }
    }
}