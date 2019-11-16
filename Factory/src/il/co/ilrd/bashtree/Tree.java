package il.co.ilrd.bashtree;

import il.co.ilrd.factory.Factory;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Matan Keler
 */
public class Tree {
    private final DirFile root;
    private final Factory<String, DirContainer, File> factory = new Factory<>();
    private final Factory<String, String, java.io.File> factory2 = new Factory<>();

    {
        factory.add("dir", filedis->new DirFile(filedis.getDepth(), filedis.getName()));
        factory.add("file",filedis->new RegularFile(filedis.getDepth(), filedis.getName()));
        factory2.add("javaFile", str->new java.io.File(str));
    }

    /**
     * @throws InvalidPathException if path not valid
     */
    public Tree (String path) throws InvalidPathException{
        java.io.File f = factory2.create("javaFile", path);
        if (!f.exists()) {
            throw new InvalidPathException(path, "path not valid");
        }


        root = (DirFile) factory.create("dir", new DirContainer(0, f.getName()));
        root.lst.add((DirFile)factory.create("dir", new DirContainer(0, path)));
        root.CreateTree(path);
        root.SortAll();
    }

    private class DirContainer {
        int depth;
        String name;

        public DirContainer(int depth, String name) {
            this.depth = depth;
            this.name = name;
        }

        private int getDepth() { return depth; }
        private String getName() { return name; }
    }

    /**
     * print the tree of files accourding to the path given in constructor
     */
    public void print() {
        for (File f : root.lst) {
            f.print();
        }
    }

    public interface File {
        public void print();
        public String getName();
    }

    public class DirFile implements File {
        private final int depth;
        private final String name;
        private final List<File> lst = new ArrayList<File>();

        public DirFile(int depth, String name) {
            this.depth = depth;
            this.name = name;
        }

        private void CreateTree(String path) {
            java.io.File[] f = factory2.create("javaFile", path).listFiles();
            if (null == f) {
                return;
            }

            for (java.io.File file: f) {
                if (file.isDirectory() && !file.isHidden()) {
                    DirFile df = (DirFile) factory.create("dir", new DirContainer(depth + 1, file.getName()));
                    lst.add(df);
                    df.CreateTree(file.toString());
                }
                else if (!file.isHidden()) {
                    lst.add((RegularFile)factory.create("file", new DirContainer(depth + 1, file.getName())));
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

    public class RegularFile implements File {
        private final int depth;
        private final String name;

        public RegularFile(int depth, String name) {
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