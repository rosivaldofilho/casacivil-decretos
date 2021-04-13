/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author rosivaldo
 */
public class Utilitarios {

    public static Date getDataSemHoras(Date aDate) {
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(aDate);
        myCalendar.set(Calendar.HOUR_OF_DAY, 0);
        myCalendar.set(Calendar.MINUTE, 0);
        myCalendar.set(Calendar.SECOND, 0);
        myCalendar.set(Calendar.MILLISECOND, 0);
        return myCalendar.getTime();
    }

    public static String formataUri(String uri) {
        return uri.replaceAll("\\s+", "+");
    }

    public static String CPFRemoveTracos(String cpf) {
        String saida = cpf.replace("\\.", "");
        return saida.replaceAll("\\-", "");
    }

    public static String formataNome(String nome) {
        String saida = "";
        for (String p : nome.toLowerCase().replaceAll("\\s+", " ").split(" ")) {
            if (p.length() > 3) {
                saida += p.substring(0, 1).toUpperCase();
            } else {
                saida += p.substring(0, 1);
            }
            saida += p.substring(1).toLowerCase() + " ";
        }
        return saida;
    }

    public static Date formataData(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = sdf.parse(data);
        return convertedDate;
    }

    public static Date formataDataBR(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = sdf.parse(data);
        return convertedDate;
    }
    
    public static String formataDataBRExtenso(Date data){
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));        
        String dataExtenso = formatador.format(data);
        int index  = dataExtenso.indexOf(",") + 2;
        int lenght = dataExtenso.length();
        return dataExtenso.substring(index, lenght).toLowerCase();
    }

    public static String formataDataBR(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String convertedDate = sdf.format(data);
        return convertedDate;
    }

    public static String geraIdbyData(String id) {
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String convertedDate = sdf.format(data);
        convertedDate += id;
        return convertedDate;
    }

    public static String formataFloat(float valor, String formato) {
        DecimalFormat df = new DecimalFormat(formato);
        return df.format(valor);
    }

    public static Date getPrimeiroDiaMes(Calendar mes) throws ParseException {
        return formataDataBR(01 + "/" + (mes.get(Calendar.MONTH) + 1) + "/" + mes.get(Calendar.YEAR));
    }

    public static Date getUltimoDiaMes(Calendar mes) throws ParseException {
        return formataDataBR(mes.getActualMaximum(Calendar.DAY_OF_MONTH)
                + "/" + (mes.get(Calendar.MONTH) + 1) + "/" + mes.get(Calendar.YEAR));
    }

    public static int converteDiaCalendar(String dia) throws ParseException {
        switch (dia) {
            case "DOMINGO":
                return Calendar.SUNDAY;
            case "SEGUNDA":
                return Calendar.MONDAY;
            case "TERCA":
                return Calendar.TUESDAY;
            case "QUARTA":
                return Calendar.WEDNESDAY;
            case "QUINTA":
                return Calendar.THURSDAY;
            case "SEXTA":
                return Calendar.FRIDAY;
            case "SABADO":
                return Calendar.SATURDAY;
            default:
                return -1;
        }
    }
}
