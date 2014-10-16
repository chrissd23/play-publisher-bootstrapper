package de.bootstrap;

import java.util.Scanner;

public class Main {

    private static String mApplicationPackageName;
    private static String mServiceAccountMail;
    private static String mFilePath;
    private static String mDestinationPath;


    public static void main(String[] args) {

        Scanner scanInput = new Scanner(System.in);

        System.out.print("Please enter your PackageName: ");
        mApplicationPackageName = scanInput.nextLine();

        System.out.print("Please enter your ServiceAccountMail: ");
        mServiceAccountMail = scanInput.nextLine();

        System.out.print("Please enter your absolute Path to pk12 File: ");
        mFilePath = scanInput.nextLine();

        System.out.print("Please enter your destination Path: ");
        mDestinationPath = scanInput.nextLine();


        BootstrapTask.createListings(mApplicationPackageName, mServiceAccountMail, mFilePath, mDestinationPath);
    }
}
