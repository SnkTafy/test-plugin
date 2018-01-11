
var exec = require('cordova/exec');

var PLUGIN_NAME = 'RabbitMqPlugin';

var RabbitMqPlugin = {
  echo: function(phrase, cb) {
    exec(cb, null, PLUGIN_NAME, 'setToken', [phrase]);
  },
  startConsumer: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'startConsumer', []);
  },
  stopConsumer: function(cb) {
    exec(cb, null, PLUGIN_NAME, 'stopConsumer', []);
  }
};

module.exports = RabbitMqPlugin;
