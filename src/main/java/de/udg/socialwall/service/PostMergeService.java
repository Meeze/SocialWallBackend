package de.udg.socialwall.service;

import com.google.common.base.Strings;
import de.udg.socialwall.model.generic.MergeOptions;
import de.udg.socialwall.model.generic.SocialMediaPost;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostMergeService {

    // Max color difference before a image is considered unique
    private static int COLOR_TRESHOLD = 3;

    @SafeVarargs
    public final Set<SocialMediaPost> mergeWithOptions(MergeOptions options, List<SocialMediaPost>... posts) {
        Set<SocialMediaPost> uniquePosts = new HashSet<SocialMediaPost>();
        Set<SocialMediaPost> mergedPosts = this.mergePosts(options.isOnlyImages(), posts).collect(Collectors.toSet());
        for (SocialMediaPost mergedPost: mergedPosts) {
            boolean add = true;
            for (SocialMediaPost uniquePost : uniquePosts) {
                if(checkDuplicate(uniquePost, mergedPost)) {
                    add = false;
                }
            }
            if(add) {
                uniquePosts.add(mergedPost);
            }
        }
        return uniquePosts;
    }

    /**
     *
     * @param post1
     * @param post2
     * @return true if posts are the same
     */
    private boolean checkDuplicate(SocialMediaPost post1, SocialMediaPost post2) {
        if (Objects.equals(post1.getText(), post2.getText()) || Objects.equals(post1.getImage().getUrl(), post2.getImage().getUrl())) {
            return true;
        } else return isSameImage(post1.getImage().getUrl(), post2.getImage().getUrl());
    }

    @SafeVarargs
    public final Stream<SocialMediaPost> mergePosts(boolean imageOption, List<SocialMediaPost>... posts) {
        return Arrays.stream(posts).flatMap(List::stream).filter(socialMediaPost -> !imageOption || !Strings.isNullOrEmpty(socialMediaPost.getImage().getUrl()));
    }

    /**
     * resizes images to same dimensions and compares average color of all pixels
     * @param url1 of first image
     * @param url2 of second image
     * @return if color distance of images is less than my defined treshold
     */
    private boolean isSameImage(String url1, String url2) {
        try {
            BufferedImage image1 = ImageIO.read(new URL(url1));
            BufferedImage image2 = ImageIO.read(new URL(url2));
            Dimension dim = new Dimension(100,100);
            BufferedImage resizedImg1 = Scalr.resize(image1, Scalr.Method.QUALITY ,
                    dim.width, dim.height);
            BufferedImage resizedImg2 = Scalr.resize(image2, Scalr.Method.QUALITY ,
                    dim.width, dim.height);
            int[] data1 = getPixelData(resizedImg1);
            int[] data2 = getPixelData(resizedImg2);
            return isAvgPixelColorSimilar(data1, data2);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param image to grab from
     * @return int array of pixel data
     * @throws InterruptedException if grabbing fails
     */
    private int[] getPixelData(BufferedImage image) throws InterruptedException {
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, -1, -1, false);
        int[] output = new int[0];
        if(grabber.grabPixels()) {
            output = new int[grabber.getHeight()*grabber.getWidth()];
            output = (int[]) grabber.getPixels();
        }
        return output;
    }

    /**
     * gets average color from all pixels and compares against trehsold
     * @param data1 pixel data from 1 image
     * @param data2 from anoher image
     * @return if color below treshold
     */
    private boolean isAvgPixelColorSimilar(int[] data1, int[] data2) {
        Color color1 = getAvgColor(data1);
        Color color2 = getAvgColor(data2);
        return getColorDistance(color1, color2) < COLOR_TRESHOLD;
    }

    /**
     * gets average color fromn imagedata
     * @param data pixeldata array of an image
     * @return avg color
     */
    private Color getAvgColor(int[] data) {
        int accumulatedRed = 0;
        int accumulatedGreen = 0;
        int accumulatedBlue = 0;
        for (int i : data) {
            Color color = new Color(i);
            accumulatedRed += color.getRed();
            accumulatedGreen += color.getGreen();
            accumulatedBlue += color.getBlue();
        }
        return new Color(accumulatedRed/data.length, accumulatedGreen/data.length, accumulatedBlue/data.length);
    }


    /**
     * formula to calculate the distance between to RGB colors
     * @param c1 color1
     * @param c2 color2
     * @return distance in double
     */
    private double getColorDistance(Color c1, Color c2){
        //return (Math.abs(c1.getRed() - c2.getRed()) + Math.abs(c1.getGreen() - c2.getGreen()) + Math.abs(c1.getBlue() - c2.getBlue()));
        return (c1.getRed() - c2.getRed())*(c1.getRed() - c2.getRed()) + (c1.getGreen() - c2.getGreen())*(c1.getGreen() - c2.getGreen()) + (c1.getBlue() - c2.getBlue())*(c1.getBlue() - c2.getBlue());
    }


}
