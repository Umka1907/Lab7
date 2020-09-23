package data;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
    public static String encryptThisString(String input)

    {

        try {


            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] messageDigest = md.digest(input.getBytes());



            // Преобразование байтового массива в представление знака

            BigInteger no = new BigInteger(1, messageDigest);



            // Преобразуем дайджест сообщения в шестнадцатеричное значение

            String hashtext = no.toString(16);



            // Добавить предыдущие 0, чтобы сделать его 32-битным

            while (hashtext.length() < 32) {

                hashtext = "0" + hashtext;

            }

            // возвращаем HashText

            return hashtext;

        }

        catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);

        }

    }

}
