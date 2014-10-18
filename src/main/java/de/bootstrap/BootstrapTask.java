package de.bootstrap;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class BootstrapTask {


    public static void createListings(String applicationPackageName, String serviceAccoutnMail, String filePath, String destinationPath) {
        File pkFile = new File(filePath);
        try {
            final AndroidPublisher service = AndroidPublisherHelper.init(serviceAccoutnMail, pkFile);
            final AndroidPublisher.Edits edits = service.edits();

            AndroidPublisher.Edits.Insert editRequest = edits.insert(applicationPackageName, null /** no content */);
            AppEdit appEdit = editRequest.execute();
            String editId = appEdit.getId();

            ListingsListResponse response = edits.listings().list(applicationPackageName, editId).execute();
            List<Listing> listListings = response.getListings();

            if (listListings != null) {
                File file = new File(destinationPath + "/play");
                file.mkdir();

                String language;
                String fullDescription;
                String shortDescription;
                String title;

                for (Listing listListing : listListings) {
                    language = listListing.getLanguage();
                    fullDescription = listListing.getFullDescription();
                    shortDescription = listListing.getShortDescription();
                    title = listListing.getTitle();

                    File languageFolder = new File(file.getAbsolutePath() + "/" + language);
                    languageFolder.mkdir();
                    if (languageFolder.exists()) {
                        File listing = new File(languageFolder.getAbsolutePath() + "/listings");
                        listing.mkdir();
                        if (listing.exists()) {
                            FileUtils.writeStringToFile(new File(listing.getAbsolutePath() + "/fulldescription"), fullDescription);
                            FileUtils.writeStringToFile(new File(listing.getAbsolutePath() + "/shortdescription"), shortDescription);
                            FileUtils.writeStringToFile(new File(listing.getAbsolutePath() + "/title"), title);
                        }

                    }
                }
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createWhatsNew(String applicationPackageName, String serviceAccoutnMail, String filePath, String destinationPath, String mVersionCode) {
        File pkFile = new File(filePath);
        try {
            final AndroidPublisher service = AndroidPublisherHelper.init(serviceAccoutnMail, pkFile);
            final AndroidPublisher.Edits edits = service.edits();

            AndroidPublisher.Edits.Insert editRequest = edits.insert(applicationPackageName, null /** no content */);
            AppEdit appEdit = editRequest.execute();
            String editId = appEdit.getId();

            ApkListingsListResponse apkListingsListResponse = edits.apklistings().list(applicationPackageName, editId, Integer.valueOf(mVersionCode)).execute();

            List<ApkListing> listings = apkListingsListResponse.getListings();

            if (listings != null) {
                File file = new File(destinationPath + "/play");
                if (!file.exists()) {
                    file.mkdir();
                }

                String language;
                String whatsNew;

                for (ApkListing apkListing : listings) {
                    language = apkListing.getLanguage();
                    whatsNew = apkListing.getRecentChanges();

                    File languageFolder = new File(file.getAbsolutePath() + "/" + language);
                    if (!languageFolder.exists()) {
                        languageFolder.mkdir();
                    }
                    if (languageFolder.exists()) {
                        FileUtils.writeStringToFile(new File(languageFolder.getAbsolutePath() + "/whatsnew"), whatsNew);
                    }
                }
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}