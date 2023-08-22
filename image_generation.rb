require 'chunky_png'

def generate_image(image_path, query, title, subtitle)
  image = ChunkyPNG::Image.from_file(image_path)
  width = image.width
  height = image.height

  pixel_colors = []

  (0...width).each do |x|
    (0...height).each do |y|
      color = ChunkyPNG::Color.rgba(*image[x, y])
      pixel_colors << color
    end
  end

  average_color = ChunkyPNG::Color.rgba(
    pixel_colors.map { |color| ChunkyPNG::Color.r(color) }.sum / pixel_colors.length,
    pixel_colors.map { |color| ChunkyPNG::Color.g(color) }.sum / pixel_colors.length,
    pixel_colors.map { |color| ChunkyPNG::Color.b(color) }.sum / pixel_colors.length,
    255
  )

  background_image = ChunkyPNG::Image.new(1000, 1000, average_color)

  circle_image = ChunkyPNG::Image.new(512, 512, ChunkyPNG::Color.rgba(255, 255, 255, 0))
  circle_image.circle(256, 256, 256, ChunkyPNG::Color.rgba(255, 255, 255, 255), 10)

  max_size = [width, height].max
  resize_factor = 512.0 / max_size
  resized_image = image.resize((width * resize_factor).to_i, (height * resize_factor).to_i)

  offset_x = (1000 - resized_image.width) / 2
  offset_y = (1000 - resized_image.height) / 2

  background_image.compose!(circle_image, offset_x, offset_y)
  background_image.compose!(resized_image, offset_x, offset_y)

  big_font = ChunkyPNG::Font.from_file('assets/font/AmaticSC-Bold.ttf', 80)
  small_font = ChunkyPNG::Font.from_file('assets/font/AmaticSC-Regular.ttf', 45)

  text_color = ChunkyPNG::Color.rgba(255, 255, 255, 255)
  text1_size = big_font.get_text_size(title)
  text2_size = small_font.get_text_size(subtitle)

  background_image.compose_text!(
    (1000 - text1_size.width) / 2,
    offset_y + resized_image.height + 50,
    title,
    font: big_font,
    color: text_color
  )

  background_image.compose_text!(
    (1000 - text2_size.width) / 2,
    offset_y + resized_image.height + text1_size.height + 60,
    subtitle,
    font: small_font,
    color: text_color
  )

  background_image.save("assets/second/#{query}.png")
end

generate_image('image.png', 'query', 'Title', 'Subtitle')
