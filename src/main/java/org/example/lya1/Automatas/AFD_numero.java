package org.example.lya1.Automatas;

public class AFD_numero {
    /*public static void main(String args[]){
        System.out.println(evaluar("123."));
    }*/
    private static boolean flag;
    public static boolean evaluar(String cad){
        q0(cad,0);
        return flag;
    }
    public static void q0(String cad, int pos){
        if(pos<cad.length()){
            switch (cad.charAt(pos)){
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    q2(cad,pos+1);
                    break;
                case '.':
                    q1();
                    break;
                default:
                    flag=false;
            }
        }else
            flag=false;
    }
    private static void q1(){
        flag=false;
    }
    private static void q2(String cad, int pos)/*Estado e aceptacion*/{
        if(pos<cad.length()){
            switch (cad.charAt(pos)){
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    q2(cad,pos+1);
                    break;
                case '.':
                    q3(cad, pos+1);
                    break;
                default:
                    flag=false;
            }
        }else{
            flag=true;
        }
    }
    private static void q3(String cad, int pos){
        if(pos<cad.length()){
            switch (cad.charAt(pos)){
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    q2(cad,pos+1);
                    break;
                case '.':
                    q1();
                    break;
                default:
                    flag=false;
            }
        }else{
            flag=false;
        }
    }
}
