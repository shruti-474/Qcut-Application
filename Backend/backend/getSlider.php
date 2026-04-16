<?php
if (substr_count($_SERVER['HTTP_ACCEPT_ENCODING'], 'gzip')) {
    ob_start("ob_gzhandler");
} else {
    ob_start();
}

header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Cache-Control: public, max-age=86400');

require_once("databaseconnect.php");

$sql = "SELECT id, name, image FROM imageslider LIMIT 4";
$stmt = $connection->prepare($sql);
$stmt->execute();
$result = $stmt->get_result();

$data = [];
while ($row = $result->fetch_assoc()) {
    // Trim unwanted spaces and new lines
    $row['image'] = trim($row['image']);

    // Only keep the file name (remove folder path)
    $row['image'] = basename($row['image']);

    $data[] = $row;
}

echo json_encode(['getImage' => $data], JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE);

$stmt->close();
$connection->close();
ob_end_flush();
?>
