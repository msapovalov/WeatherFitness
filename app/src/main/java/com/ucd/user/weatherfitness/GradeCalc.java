package com.ucd.user.weatherfitness;

/**
 * Created by mihhail_shapovalov on 11/17/17.
 */

public class GradeCalc

{
    public static void main(String[] args)
    {
        String s1 = new String(args[0]);
        String s2 = new String(args[1]);
        Integer i1 = new Integer(args[2]);
        Integer i2 = new Integer(args[3]);


        if ( s1.compareToIgnoreCase(s2) > 0 )
            System.out.println(s2);
        else if ( s1.compareToIgnoreCase(s2) < 0 )
            System.out.println(s1);
        else
            System.out.println("Both strings are equal.");
    }
}