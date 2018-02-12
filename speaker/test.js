var groove = require('groove');

groove.open("/usr/share/sounds/alsa/Front_Left.wav", function(err, file) {
  if (err) throw err;
  console.log(file.metadata());
  console.log("duration:", file.duration());
  file.close(function(err) {
    if (err) throw err;
  });
});


