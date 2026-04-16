<?php
require_once("databaseconnect.php");


$sql = "SELECT * FROM history";
$result = mysqli_query($connection, $sql);

$data = array();
while ($row = mysqli_fetch_assoc($result)) {
    $data[] = array(
        'id'            => $row['id'],
        'shopname'      => $row['shopname'],
        'shopaddress'   => $row['shopaddress'],
        'shopemail'     => $row['shopemail'],
        'finalname'     => $row['finalname'],
        'finalemail'    => $row['finalemail'],
        'finaldate'     => $row['finaldate'],
        'finaltime'     => $row['finaltime'],
        'finalservice'  => $row['finalservice'],
        'totalamount'   => $row['totalamount'],
        'paymentMode'   => $row['paymentMode'],
        'paymentStatus' => $row['paymentStatus'],
        'Appoinmentstatus' => $row['Appoinmentstatus']
    );
}

header('Content-Type: application/json');
echo json_encode(array('getOrders' => $data));
?>
