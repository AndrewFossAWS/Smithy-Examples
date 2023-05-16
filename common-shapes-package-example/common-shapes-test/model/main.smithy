$version: "1.0"

namespace example

use example.common#UuidV4
use example.common.enums#IpAddressVersion
use example.common.integers#PositiveDurationInSeconds
use example.common.strings#AlphaNumericName
use example.common.strings#IpV4Address

structure MyTestStructure {
    id: UuidV4
    name: AlphaNumericName
    ipAddr: IpV4Address
    ipAddrVersion: IpAddressVersion
    duration: PositiveDurationInSeconds
}

string ChecksThatValidatorsWereNotIncludedInCommonShapesPackage
