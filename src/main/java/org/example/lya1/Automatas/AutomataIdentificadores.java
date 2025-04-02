package org.example.lya1.Automatas;

public class AutomataIdentificadores {
        public static final String [] letras={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","ñ","o","p","q","r","s","t","u","v","w","x","y","z",
                                            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","Ñ","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        public static final String [] numeros={"0","1","2","3","4","5","6","7","8","9"};
        public static final String [] simbolos={"@","_"};

        public static boolean afdIdentificadores(String cadena){
            boolean flag=true;
            String cadena_split="";
            for(int i=0; i<cadena.length(); i++){cadena_split=cadena.charAt(i)+" ";}
            String [] array_token=cadena_split.split(" ");
            if(array_token[0].equals("@")){
                for(int i=1; i< array_token.length && flag; i++){
                    String buscar=array_token[i];
                    for(String caracter: letras){
                        if(!buscar.equals(caracter)){
                            flag=false;
                            break;
                        }
                    }
                    if(buscar.equals("@"))
                        flag=false;
                    for(String numero: numeros){
                        if(!buscar.equals(numero)){
                            flag=false;
                            break;
                        }
                    }
                }
            }else{
                flag=false;
            }
            return flag;
        }
}
