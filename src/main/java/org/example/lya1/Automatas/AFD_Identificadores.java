package org.example.lya1.Automatas;

public class AFD_Identificadores {
    private static boolean flag;
    public static final String [] letras={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","ñ","o","p","q","r","s","t","u","v","w","x","y","z",
                                            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","Ñ","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    public static final String [] numeros={"0","1","2","3","4","5","6","7","8","9"};
    public static final String [] simbolos={"@","_"};
    public static boolean evaluar(String cad){
        flag=true;
        q0(cad,0);
        return flag;
    }

    private static void q0(String cad, int pos){
        if(pos<cad.length()){
            if(cad.charAt(pos)=='@')
                q2(cad, pos+1);
            else
                q1();
        }else
            flag=false;
    }
    private static void q1(){flag=false;}
    private static void q2(String cad, int pos){
        if(pos<cad.length()){
            boolean contains=false;
            String car=(cad.charAt(pos)+"");
            for(String letra: letras){if(car.equals(letra)){contains=true;}}
            if(!contains){
                for(String numero:numeros){if(car.equals(numero)){contains=true;}}
                if(!contains){
                    if(car.equals("_"))
                        contains=true;
                }
            }
            if (contains)
                q3(cad, pos+1);
            else
                q1();
        }else{
            flag=false;
        }
    }
    private static void q3(String cad, int pos){
        if(pos<cad.length()){
            boolean contains=false;
            String car=(cad.charAt(pos)+"");
            for(String letra: letras){if(car.equals(letra)){contains=true;}}
            if(!contains){
                for(String numero:numeros){if(car.equals(numero)){contains=true;}}
                if(!contains){
                    if(car.equals("_"))
                        contains=true;
                }
            }
            if (contains)
                q3(cad, pos+1);
            else
                q1();
        }else{
            flag=true;
        }
    }
}
