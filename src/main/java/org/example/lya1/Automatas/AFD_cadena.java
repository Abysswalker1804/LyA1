package org.example.lya1.Automatas;

public class AFD_cadena {
    private static boolean flag=true;

    public static boolean evaluar(String cadena){
        q0(cadena,0);
        return flag;
    }

    private static void q0(String cadena, int pos){
        if(pos<cadena.length()){
            if(cadena.charAt(pos)=='"')
                q1(cadena,pos+1);
            else
                q2();
        }else
            flag=false;
    }
    private static void q1(String cadena, int pos){
        if(pos<cadena.length()){
            if(cadena.charAt(pos)=='"')
                q3(cadena, pos+1);
            else
                q1(cadena, pos+1);
        }else
            flag=false;
    }
    private static void q2(){
        flag=false;
    }
    private static void q3(String cadena, int pos){
        if (pos<cadena.length())
            q2();
        else
            flag=true;
    }
}
