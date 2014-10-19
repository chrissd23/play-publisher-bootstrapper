package de.bootstrap;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;

public class BootstrapTask {

    private static final String IMAGE_TYPE_FEATURE_GRAPHIC = "featureGraphic";
    private static final String IMAGE_TYPE_ICON = "icon";
    private static final String IMAGE_TYPE_PHONE_SCREENSHOTS = "phoneScreenshots";
    private static final String IMAGE_TYPE_PROMO_GRAPHIC = "promoGraphic";
    private static final String IMAGE_TYPE_SEVEN_INCH_SCREENSHOTS = "sevenInchScreenshots";
    private static final String IMAGE_TYPE_TEN_INCH_SCREENSHOTS = "tenInchScreenshots";

    private static final String[] IMAGE_TYPE_ARRAY = {
            IMAGE_TYPE_ICON,
            IMAGE_TYPE_FEATURE_GRAPHIC,
            IMAGE_TYPE_PHONE_SCREENSHOTS,
            IMAGE_TYPE_SEVEN_INCH_SCREENSHOTS,
            IMAGE_TYPE_TEN_INCH_SCREENSHOTS,
            IMAGE_TYPE_PROMO_GRAPHIC};


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

                            for (String imageType : IMAGE_TYPE_ARRAY) {
                                ImagesListResponse imagesListResponse = edits.images().list(applicationPackageName, editId, language, imageType).execute();
                                saveImage(listing.getAbsolutePath(), imageType, imagesListResponse);
                            }

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

    private static void saveImage(String path, String imageFolderName, ImagesListResponse imagesListResponse) {
        File imageFolder = new File(path + "/" + imageFolderName);
        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        }
        List<Image> images = imagesListResponse.getImages();
        if (images != null) {
            for (Image image : images) {
                try {
                    downloadImageFromUrl(image.getUrl(), imageFolder.getAbsolutePath() + "/" + image.getId() + ".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void downloadImageFromUrl(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }


}