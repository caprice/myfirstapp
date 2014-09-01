package com.mmm.mvideo.business.entity;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class NavigationLine.
 * @author a37wczz
 */
public class NavigationLine implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The title. */
    private String title;

    /** The image. */
    private String image;

    /** The desc. */
    private String desc;

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the image.
     * 
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image.
     * 
     * @param image
     *            the new image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets the desc.
     * 
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the desc.
     * 
     * @param desc
     *            the new desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

}
