package com.czr.code;

import java.io.File;

/**
 * @author chenzhirong
 * @createtime 2020-09-09 13:10
 */
public class MetaInfo {

    public static void main(String[] args) {
        File file = new File("D:\\MyWorkspaces\\001MyWorkSpaces\\001frame\\01CodeGenerator\\Aj.CodeGenerator\\lib");
        if(file != null && file.listFiles().length > 0){
            for (File f : file.listFiles()) {
                System.out.println(" " +"lib/"+ f.getName() + " ");
            }
        }
    }
}
