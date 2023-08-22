# Secondhand Image Generator

This is a Python script that generates stylized images similar to those in "Secondhand" from Cat bot using the Python Imaging Library (PIL). The script takes an input image and generates a new image by performing various operations such as color analysis, resizing, overlaying shapes, and adding text.

## Requirements

To run this script, you'll need to have the Python Imaging Library (PIL) installed. You can install it using the following command:

```bash
pip install Pillow
```

## Usage

To use this script, you can call the `generate_image` function, passing in the required parameters:

```python
from PIL import Image, ImageDraw, ImageFont

async def generate_image(image_path: str, query: str, title: str, subtitle: str) -> None:
    # ... (rest of the code)

# Example usage
image_path = 'path/to/your/input/image.png'
query = 'image_query'
title = 'Generated Image Title'
subtitle = 'Generated Image Subtitle'

await generate_image(image_path, query, title, subtitle)
```

Replace the `image_path`, `query`, `title`, and `subtitle` with appropriate values.

## How It Works

1. The script opens the input image and converts it to RGBA format.
2. It analyzes the pixel colors of the input image to calculate an average color.
3. A new background image is created using the calculated average color.
4. A white circle with a transparent background is drawn using PIL's `ImageDraw` module.
5. The input image is resized proportionally and overlaid onto the background.
6. Text elements are added using custom fonts and positioned at the bottom of the image.
7. The generated image is saved in the `assets/second/` directory with the filename derived from the `query`.

## Image example № 1:

![Generated Image](https://github.com/hatetea/Secondhand-Image-Generation/blob/main/assets/second/image1_example.png)


## Image example № 2:

![Generated Image](https://github.com/hatetea/Secondhand-Image-Generation/blob/main/assets/second/image2_example.png)


---
влад скэ соси хз
