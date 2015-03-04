///////////////////////////////////////////////////////////////////////////////
//PROJECT:       Micro-Manager
//SUBSYSTEM:
//-----------------------------------------------------------------------------
//
// AUTHOR:       Chris Weisiger, 2015
//
// COPYRIGHT:    University of California, San Francisco, 2015
//
// LICENSE:      This file is distributed under the BSD license.
//               License text is included with the source distribution.
//
//               This file is distributed in the hope that it will be useful,
//               but WITHOUT ANY WARRANTY; without even the implied warranty
//               of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
//               IN NO EVENT SHALL THE COPYRIGHT OWNER OR
//               CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
//               INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.

package org.micromanager.data;

import mmcorej.TaggedImage;

/**
 * An Image is a single image plane with associated metadata. Functionally 
 * similar to TaggedImage, but with more rigidly-defined metadata and 
 * dataset positioning information.
 * You are not expected to implement this interface; it is here to describe how
 * you can interact with Images created by Micro-Manager itself.
 */
public interface Image {
   /**
    * Return a reference to whatever entity stores the actual pixel data for
    * this Image. Is most likely a byte[] or short[] but could be of any
    * primitive type.
    * @return An array of pixel values for the image data
    */
   public Object getRawPixels();

   /**
    * As getRawPixels(), but will split out the specified component for
    * multi-component images. This could potentially impair performance as
    * the image data must be manually de-interleaved. Calling this with an
    * argument of 0 is equivalent to calling getRawPixels() for
    * single-component images, except that a copy of the pixels will be made.
    * @param component The component number, starting from 0
    * @return An array of pixel values for the specified component
    */
   public Object getRawPixelsForComponent(int component);

   /**
    * Generate a copy of this Image, except that its Coords object is at a
    * different location, as specified.
    * This is a "shallow copy", thus the new image and the original image will
    * share their image data and metadata.
    * @param coords Coordinates at which to place the new image.
    * @return The copied image
    */
   public Image copyAtCoords(Coords coords);

   /**
    * Generate a copy of this Image, except that its Metadata object uses
    * the provided Metadata.
    * This is a "shallow copy", thus the new image and the original image will
    * share their image data and coordinates.
    * @param metadata The new metadata to use for the copy
    * @return The copied image
    */
   public Image copyWithMetadata(Metadata metadata);

   /**
    * Generate a copy of this Image, except that its Coords object is at the
    * provided location, and it uses the provided Metadata.
    * This is a "shallow copy", thus the new image and the original image will
    * share their image data.
    * Equivalent to chaining copyAtCoords() and copyWithMetadata().
    * @param coords Coordinates at which to place the new image.
    * @param metadata The new metadata to use for the copy
    * @return The copied image
    */
   public Image copyWith(Coords coords, Metadata metadata);

   /**
    * Retrieve the intensity of the pixel at the specified position. Not
    * guaranteed to work well for all image types (e.g. RGB images will still
    * get only a single value, which may be an odd summation of the values
    * of the different components). Will throw an
    * ArrayIndexOutOfBoundsException if the coordinates exceed the size of the
    * image.
    * Equivalent to calling getComponentIntensityAt(x, y, 0).
    * @param x X coordinate at which to retrieve image data
    * @param y Y coordinate at which to retrieve image data
    * @return intensity of the image at the specified coordinates
    */
   public long getIntensityAt(int x, int y);

   /**
    * For multi-component (e.g. RGB) images, extract the value of the specified
    * component at the given pixel location. Not guaranteed to make any kind of
    * sense if called on single-component images with a nonzero "component"
    * value.
    * @param x X coordinate at which to retrieve image data
    * @param y Y coordinate at which to retrieve image data
    * @param component The component number to retrieve intensity for, starting
    *        from 0.
    * @return intensity of the image at the specified coordinates, for the
    *         given component
    */
   public long getComponentIntensityAt(int x, int y, int component);

   /**
    * Generate a string describing the value(s) of the pixel at the specified
    * location. The string will be a plain number for single-component images,
    * and an "[A/B/C]"-formatted string for multi-component images.
    * @param x X coordinate at which to retrieve image data
    * @param y Y coordinate at which to retrieve image data
    * @return A string describing the pixel intensity/intensities at the given
    *         coordinates.
    */
   public String getIntensityStringAt(int x, int y);

   /**
    * Retrieve the Metadata for this Image.
    * @return The image metadata.
    */
   public Metadata getMetadata();

   /**
    * Retrieve the Coords for this Image.
    * @return The image coordinates.
    */
   public Coords getCoords();

   /**
    * Get the width of the image in pixels.
    * @return The width of the image in pixels.
    */
   public int getWidth();

   /**
    * Get the height of the image in pixels.
    * @return The height of the image in pixels.
    */
   public int getHeight();

   /**
    * Get the number of bytes used to represent each pixel in the raw pixel
    * data.
    * @return The number of bytes per pixel in the image.
    */
   public int getBytesPerPixel();

   /**
    * Get the number of components (e.g. for RGB images) in each pixel in the
    * raw pixel data. Will be 1 for single-component images.
    * @return The number of components comprising the image.
    */
   public int getNumComponents();
}
