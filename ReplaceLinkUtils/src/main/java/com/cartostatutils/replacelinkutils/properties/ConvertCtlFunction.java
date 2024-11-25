package com.cartostatutils.replacelinkutils.properties;

import java.io.File;

@FunctionalInterface
interface ConvertCtlFunction {
    boolean check(File f);
    
}
