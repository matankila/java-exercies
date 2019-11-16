package il.co.ilrd.linkedlist;

import java.io.IOException;

public class EmptyListException extends IOException {
    EmptyListException(String msg) {
        super(msg);
    }
}
