const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const config = {
    mode: 'none',
    entry: {
        index: path.resolve(__dirname, 'source/index.js'),
    },
    output: {
        path: path.resolve(__dirname, '../service/src/main/webapp/'),
        filename: '[name]-[chunkhash].js'
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: path.resolve(__dirname, 'source/index.html'),
            inject: true
        })
    ],
    resolve: {
        alias: {
            'vue$': 'vue/dist/vue.esm.js' // 用 webpack 1 时需用 'vue/dist/vue.common.js'
        }
    }
};

module.exports = config;
