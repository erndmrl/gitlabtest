kill $(ps aux | grep 'chromedrive[r]' | awk '{print $2}');
kill $(ps aux | grep 'geckodrive[r]' | awk '{print $2}');
kill $(ps aux | grep 'remotedrive[r]' | awk '{print $2}');
kill $(ps aux | grep 'webdrive[r]' | awk '{print $2}');
kill $(ps aux | grep 'remotewebdrive[r]' | awk '{print $2}');
kill $(ps aux | grep 'marionette' | awk '{print $2}');
kill $(ps aux | grep 'marionettedrive[r]' | awk '{print $2}')
