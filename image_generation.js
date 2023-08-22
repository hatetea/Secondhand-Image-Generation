const { createCanvas, loadImage, registerFont } = require('canvas');

async function generateImage(imagePath, query, title, subtitle) {
    const canvas = createCanvas(1000, 1000);
    const ctx = canvas.getContext('2d');

    const image = await loadImage(imagePath);
    const width = image.width;
    const height = image.height;

    ctx.drawImage(image, 0, 0, width, height, 0, 0, 1000, 1000);

    const imageData = ctx.getImageData(0, 0, 1000, 1000);
    const pixelData = imageData.data;

    let rSum = 0;
    let gSum = 0;
    let bSum = 0;
    let count = 0;

    for (let i = 0; i < pixelData.length; i += 4) {
        const r = pixelData[i];
        const g = pixelData[i + 1];
        const b = pixelData[i + 2];
        const a = pixelData[i + 3];

        rSum += r;
        gSum += g;
        bSum += b;
        count++;
    }

    const averageR = Math.floor(rSum / count);
    const averageG = Math.floor(gSum / count);
    const averageB = Math.floor(bSum / count);

    ctx.fillStyle = `rgba(${averageR}, ${averageG}, ${averageB}, 1)`;
    ctx.fillRect(0, 0, 1000, 1000);

    ctx.beginPath();
    ctx.arc(500, 500, 256, 0, Math.PI * 2);
    ctx.fillStyle = 'rgba(255, 255, 255, 1)';
    ctx.fill();
    ctx.lineWidth = 10;
    ctx.strokeStyle = 'rgba(255, 255, 255, 1)';
    ctx.stroke();

    const maxSize = Math.max(width, height);
    const resizeFactor = 512 / maxSize;
    const resizedWidth = width * resizeFactor;
    const resizedHeight = height * resizeFactor;

    ctx.drawImage(image, 0, 0, width, height, (1000 - resizedWidth) / 2, (1000 - resizedHeight) / 2, resizedWidth, resizedHeight);

    ctx.font = '80px "Arial"';
    ctx.fillStyle = 'white';
    const text1Width = ctx.measureText(title).width;
    ctx.fillText(title, (1000 - text1Width) / 2, (1000 + resizedHeight) / 2 + 50);

    ctx.font = '45px "Arial"';
    const text2Width = ctx.measureText(subtitle).width;
    ctx.fillText(subtitle, (1000 - text2Width) / 2, (1000 + resizedHeight) / 2 + 50 + 80);

    const fs = require('fs');
    const out = fs.createWriteStream(`assets/second/${query}.png`);
    const stream = canvas.createPNGStream();
    stream.pipe(out);
}

generateImage('image.png', 'query', 'Title', 'Subtitle');
