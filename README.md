# Colour Reader

A simple library for analysing the colour content of images.

At present, it can perform the following analysis:

- determine overall average colour of an image
- determine colour usage by area of an image

## Usage

### `ColourReader.averageColour(img)`

Returns a single `HSBColour` instance, containting the hue, saturation and
brightness values of the total average colour of the input image.

#### Example:

```java
  BufferedImage img = ImageIO.read(new File('/path/to/image.jpg'));
  HSBColour avgCol = new ColourReader().averageColour(img);

  // avgColour now contains the overall average colour of the image

  // we can convert an HSBColour to a regular Java Colour instance:
  Color c = new Color(Color.HSBtoRGB(avgCol.hue(), 
                                     avgCol.saturation(), 
                                     avgCol.brightness()));
```


### `ColourReader.colourArea(img)`

Returns a list of `HSBColour`s representing the colours used in the
source image, sorted by the area they occupy, from largest to smallest.

Not *every* colour in the source image is returned, but rather (by default)
grouped by a common set of hues (red, yellow, green, cyan, etc) as well as
black, white and grey. Colours from the image within those hue ranges are then
averaged, so the resulting list would contain the average shade of red, blue,
green etc. from the image.

The hue ranges may be customised for finer results (and a larger result-set).

### Example

```java

```
