package org.kft.sql.generator;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.util.Scanner;

/**
 * generate context
 *
 * @author author
 * @since 2024/3/15
 **/
public class GenerateContext {
    public Log log;
    public String baseClass;
    public FileTemplate template;
    public File sourceCodeDirectory;
    public Scanner scanner;
}
