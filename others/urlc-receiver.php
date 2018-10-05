<?php

$content = date("F j, Y, g:i a").PHP_EOL;

$content .= file_get_contents('php://input');
$content .= PHP_EOL;
$content .= 'POST: '.json_encode($_POST).PHP_EOL;
$content .= PHP_EOL;

file_put_contents("paymentinfo.txt", $content, FILE_APPEND);

echo "OK";
?>