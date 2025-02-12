const hexo = require('hexo');

hexo.extend.filter.register('before_post_render', function(data) {
    // 这里设置文章内容的字符数，以便插入 `<!-- more -->`
    const maxLength = 1500; // 可根据需要调整

    // 如果文章的内容长度大于 maxLength，则插入 `<!-- more -->`
    if (data.content.length > maxLength && !data.content.includes('<!-- more -->')) {
        const contentWithoutFirstParagraph = data.content.replace(/<\/p>/, '</p><!-- more -->');
        data.content = contentWithoutFirstParagraph;
    }

    return data;
});