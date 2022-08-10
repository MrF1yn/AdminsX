package xyz.vectlabs.adminsx.databases;


import java.util.List;
import java.util.UUID;

public interface IDatabase {

    String name();

    boolean connect();

    void init();



}
