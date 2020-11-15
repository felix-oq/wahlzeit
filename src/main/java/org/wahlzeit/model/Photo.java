/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit.model;

import java.sql.*;
import java.net.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

/**
 * A photo represents a user-provided (uploaded) photo.
 */
public class Photo extends DataObject {

	/**
	 * 
	 */
	public static final String IMAGE = "image";
	public static final String THUMB = "thumb";
	public static final String LINK = "link";
	public static final String PRAISE = "praise";
	public static final String NO_VOTES = "noVotes";
	public static final String CAPTION = "caption";
	public static final String DESCRIPTION = "description";
	public static final String KEYWORDS = "keywords";

	public static final String TAGS = "tags";

	public static final String STATUS = "status";
	public static final String IS_INVISIBLE = "isInvisible";
	public static final String UPLOADED_ON = "uploadedOn";
	
	/**
	 * 
	 */
	public static final int MAX_PHOTO_WIDTH = 420;
	public static final int MAX_PHOTO_HEIGHT = 600;
	public static final int MAX_THUMB_PHOTO_WIDTH = 105;
	public static final int MAX_THUMB_PHOTO_HEIGHT = 150;

	/**
	 * The location where the photo was taken.
	 */
	public Location location;
	
	/**
	 * 
	 */
	protected PhotoId id = null;
	
	/**
	 * 
	 */
	protected int ownerId = 0;
	protected String ownerName;

	/**
	 * 
	 */
	protected boolean ownerNotifyAboutPraise = false;
	protected EmailAddress ownerEmailAddress = EmailAddress.EMPTY;
	protected Language ownerLanguage = Language.ENGLISH;
	protected URL ownerHomePage;
	
	/**
	 * 
	 */
	protected int width;
	protected int height;
	protected PhotoSize maxPhotoSize = PhotoSize.MEDIUM; // derived
	
	/**
	 * 
	 */
	protected Tags tags = Tags.EMPTY_TAGS;

	/**
	 * 
	 */
	protected PhotoStatus status = PhotoStatus.VISIBLE;
	
	/**
	 * 
	 */
	protected int praiseSum = 10;
	protected int noVotes = 1;
	
	/**
	 * 
	 */
	protected long creationTime = System.currentTimeMillis();
	
	/**
	 * 
	 */
	public Photo() {
		id = PhotoId.getNextId();
		incWriteCount();
	}
	
	/**
	 * 
	 * @methodtype constructor
	 */
	public Photo(PhotoId myId) {
		id = myId;
		
		incWriteCount();
	}
	
	/**
	 * 
	 * @methodtype constructor
	 */
	public Photo(ResultSet rset) throws SQLException {
		readFrom(rset);
	}

	/**
	 * 
	 * @methodtype get
	 */
	public String getIdAsString() {
		return String.valueOf(id.asInt());
	}
	
	/**
	 * 
	 */
	public void readFrom(ResultSet rset) throws SQLException {
		id = PhotoId.getIdFromInt(rset.getInt("id"));

		ownerId = rset.getInt("owner_id");
		ownerName = rset.getString("owner_name");
		
		ownerNotifyAboutPraise = rset.getBoolean("owner_notify_about_praise");
		ownerEmailAddress = EmailAddress.getFromString(rset.getString("owner_email_address"));
		ownerLanguage = Language.getFromInt(rset.getInt("owner_language"));
		ownerHomePage = StringUtil.asUrl(rset.getString("owner_home_page"));

		width = rset.getInt("width");
		height = rset.getInt("height");

		tags = new Tags(rset.getString("tags"));

		status = PhotoStatus.getFromInt(rset.getInt("status"));
		praiseSum = rset.getInt("praise_sum");
		noVotes = rset.getInt("no_votes");

		creationTime = rset.getLong("creation_time");

		maxPhotoSize = PhotoSize.getFromWidthHeight(width, height);

		location = createLocationFrom(rset);
	}

	/**
	 * Creates a location that is initialized with coordinate components from the provided result set
	 * @param rset the result set to read the values from
	 * @return a location instance with coordinate components from the provided result set or null if the location
	 * is underspecified (iff any coordinate component delivers true for {@link Double#isNaN()})
	 * @throws SQLException if the necessary values cannot be retrieved from the provided result set
	 * @throws NullPointerException if the provided argument is null
	 */
	private static Location createLocationFrom(ResultSet rset) throws SQLException {
		double coordinateComponentX = rset.getDouble("coordinate_x");
		double coordinateComponentY = rset.getDouble("coordinate_y");
		double coordinateComponentZ = rset.getDouble("coordinate_z");

		if (Double.isNaN(coordinateComponentX)
				|| Double.isNaN(coordinateComponentY)
				|| Double.isNaN(coordinateComponentZ)) {
			// the location in the result set is underspecified, so return null
			return null;
		}

		Coordinate coordinate = new Coordinate(coordinateComponentX, coordinateComponentY, coordinateComponentZ);

		return new Location(coordinate);
	}
	
	/**
	 * 
	 */
	public void writeOn(ResultSet rset) throws SQLException {
		rset.updateInt("id", id.asInt());
		rset.updateInt("owner_id", ownerId);
		rset.updateString("owner_name", ownerName);
		rset.updateBoolean("owner_notify_about_praise", ownerNotifyAboutPraise);
		rset.updateString("owner_email_address", ownerEmailAddress.asString());
		rset.updateInt("owner_language", ownerLanguage.asInt());
		rset.updateString("owner_home_page", ownerHomePage.toString());
		rset.updateInt("width", width);
		rset.updateInt("height", height);
		rset.updateString("tags", tags.asString());
		rset.updateInt("status", status.asInt());
		rset.updateInt("praise_sum", praiseSum);
		rset.updateInt("no_votes", noVotes);
		rset.updateLong("creation_time", creationTime);

		writeLocationOn(rset, location);
	}

	/**
	 * Writes the contents of a location on the provided result set.
	 * @param rset the result set to write the values on
	 * @throws SQLException if the necessary values cannot be written on the provided result set
	 * @throws NullPointerException if any of the arguments is null
	 */
	private static void writeLocationOn(ResultSet rset, Location location) throws SQLException {
		Coordinate coordinate = location.coordinate;
		if (coordinate == null) {
			// there is no coordinate for this photo, so enter NaN as coordinate components
			rset.updateDouble("coordinate_x", Double.NaN);
			rset.updateDouble("coordinate_y", Double.NaN);
			rset.updateDouble("coordinate_z", Double.NaN);
		} else {
			rset.updateDouble("coordinate_x", coordinate.getX());
			rset.updateDouble("coordinate_y", coordinate.getY());
			rset.updateDouble("coordinate_z", coordinate.getZ());
		}
	}

	/**
	 * 
	 */
	public void writeId(PreparedStatement stmt, int pos) throws SQLException {
		stmt.setInt(pos, id.asInt());
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public PhotoId getId() {
		return id;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerId(int newId) {
		ownerId = newId;
		incWriteCount();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getOwnerName() {
		return ownerName;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerName(String newName) {
		ownerName = newName;
		incWriteCount();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getSummary(ModelConfig cfg) {
		return cfg.asPhotoSummary(ownerName);
	}

	/**
	 * 
	 * @methodtype get
	 */
	public String getCaption(ModelConfig cfg) {
		return cfg.asPhotoCaption(ownerName, ownerHomePage);
	}

	/**
	 * 
	 * @methodtype get
	 */
	public boolean getOwnerNotifyAboutPraise() {
		return ownerNotifyAboutPraise;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerNotifyAboutPraise(boolean newNotifyAboutPraise) {
		ownerNotifyAboutPraise = newNotifyAboutPraise;
		incWriteCount();
	}

	/**
	 * 
	 * @methodtype get
	 */
	public EmailAddress getOwnerEmailAddress() {
		return ownerEmailAddress;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerEmailAddress(EmailAddress newEmailAddress) {
		ownerEmailAddress = newEmailAddress;
		incWriteCount();
	}

	/**
	 * 
	 */
	public Language getOwnerLanguage() {
		return ownerLanguage;
	}
	
	/**
	 * 
	 */
	public void setOwnerLanguage(Language newLanguage) {
		ownerLanguage = newLanguage;
		incWriteCount();
	}

	/**
	 * 
	 * @methodtype get
	 */
	public URL getOwnerHomePage() {
		return ownerHomePage;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setOwnerHomePage(URL newHomePage) {
		ownerHomePage = newHomePage;
		incWriteCount();
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasSameOwner(Photo photo) {
		return photo.getOwnerEmailAddress().equals(ownerEmailAddress);
	}

	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean isWiderThanHigher() {
		return (height * MAX_PHOTO_WIDTH) < (width * MAX_PHOTO_HEIGHT);
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getThumbWidth() {
		return isWiderThanHigher() ? MAX_THUMB_PHOTO_WIDTH : (width * MAX_THUMB_PHOTO_HEIGHT / height);
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public int getThumbHeight() {
		return isWiderThanHigher() ? (height * MAX_THUMB_PHOTO_WIDTH / width) : MAX_THUMB_PHOTO_HEIGHT;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setWidthAndHeight(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;

		maxPhotoSize = PhotoSize.getFromWidthHeight(width, height);

		incWriteCount();
	}
	
	/**
	 * Can this photo satisfy provided photo size?
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasPhotoSize(PhotoSize size) {
		return maxPhotoSize.asInt() >= size.asInt();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public PhotoSize getMaxPhotoSize() {
		return maxPhotoSize;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public double getPraise() {
		return (double) praiseSum / noVotes;
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public String getPraiseAsString(ModelConfig cfg) {
		return cfg.asPraiseString(getPraise());
	}
	
	/**
	 * 
	 */
	public void addToPraise(int value) {
		praiseSum += value;
		noVotes += 1;
		incWriteCount();
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean isVisible() {
		return status.isDisplayable();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public PhotoStatus getStatus() {
		return status;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setStatus(PhotoStatus newStatus) {
		status = newStatus;
		incWriteCount();
	}
	
	/**
	 * 
	 * @methodtype boolean-query
	 */
	public boolean hasTag(String tag) {
		return tags.hasTag(tag);
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public Tags getTags() {
		return tags;
	}

	/**
	 * 
	 * @methodtype set
	 */
	public void setTags(Tags newTags) {
		tags = newTags;
		incWriteCount();
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public long getCreationTime() {
		return creationTime;
	}
	
}
