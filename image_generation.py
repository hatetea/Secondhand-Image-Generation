from PIL import Image, ImageDraw, ImageFont

def generate_image(image_path: str, query: str, title: str, subtitle: str) -> None:
	
	image = Image.open(image_path)
	image = image.convert('RGBA')
	width, height = image.size

	pixel_colors = []
	for x in range(width):
		for y in range(height):
			color = image.getpixel((x, y))
			if len(color) == 2:
				color = color + (0,)  #grayscale image
			pixel_colors.append(color)

	average_color = (int(sum(r for r, g, b, a in pixel_colors) / len(pixel_colors)), 
					int(sum(g for r, g, b, a in pixel_colors) / len(pixel_colors)), 
					int(sum(b for r, g, b, a in pixel_colors) / len(pixel_colors)), 
					255)


	background_image = Image.new('RGBA', (1000, 1000), average_color)

	circle_image = Image.new('RGBA', (512, 512), color=(255, 255, 255, 0))
	draw = ImageDraw.Draw(circle_image)
	draw.ellipse((0, 0, 511, 511), fill=(255, 255, 255, 255), width=10)

	max_size = max(width, height)
	resize_factor = 512 / max_size
	resized_image = image.resize((int(width * resize_factor), int(height * resize_factor)))

	offset_x = (1000 - resized_image.width) // 2
	offset_y = (1000 - resized_image.height) // 2

	background_image.alpha_composite(circle_image, (offset_x, offset_y))
	background_image.paste(resized_image, (offset_x, offset_y), resized_image)

	draw = ImageDraw.Draw(background_image)

	big_font = ImageFont.truetype('assets/font/AmaticSC-Bold.ttf', 80)
	small_font = ImageFont.truetype('assets/font/AmaticSC-Regular.ttf', 45)


	text_color = (255, 255, 255)
	text1_size = draw.textsize(title, font=big_font)
	text2_size = draw.textsize(subtitle, font=small_font)
	draw.text(((1000 - text1_size[0]) // 2, offset_y + resized_image.height + 50), title, font=big_font, fill=text_color)
	draw.text(((1000 - text2_size[0]) // 2, offset_y + resized_image.height + text1_size[1] + 60), subtitle, font=small_font, fill=text_color)
	background_image.save(f'assets/second/{query}.png')