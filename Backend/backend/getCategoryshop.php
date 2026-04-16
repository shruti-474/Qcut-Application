<?php
require_once("databaseconnect.php");
$type = $_POST['type'];

$data = array();

// Check if type is Men, Women, or Kids → also include Unisex
if ($type == "Men" || $type == "Women" || $type == "Kids") {
    $sql = "SELECT * FROM shopdata WHERE type='$type' OR type='Unisex'";
} else {
    $sql = "SELECT * FROM shopdata WHERE type='$type'";
}

$result = mysqli_query($connection, $sql);

while ($row = mysqli_fetch_array($result)) {
    array_push($data, array(
        'id' => $row[0],
        'name' => $row[1],
        'address' => $row[2],
        'time' => $row[3],
        'day' => $row[4],
        'service' => $row[5],
        'image' => $row[6],
        'type' => $row[7],
        'lattitude' => $row[8],
        'lobgitude' => $row[9],
        'shopemail'=>$row[10]
    ));
}

echo json_encode(array('getShop' => $data));
?>
