package de.bootstrap;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.AppEdit;
import com.google.api.services.androidpublisher.model.Listing;
import com.google.api.services.androidpublisher.model.ListingsListResponse;
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
                        FileUtils.writeStringToFile(new File(listing.getAbsolutePath() + "/fulldescription.txt"), fullDescription);
                        FileUtils.writeStringToFile(new File(listing.getAbsolutePath() + "/shortdescription.txt"), shortDescription);
                        FileUtils.writeStringToFile(new File(listing.getAbsolutePath() + "/title.txt"), title);
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