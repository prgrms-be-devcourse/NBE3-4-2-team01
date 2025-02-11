'use client'

import { useEffect, useState } from "react"
import { getHotelRevenue } from "@/lib/api/Booking/Revenue/RevenueApi"
import { HotelRevenueResponse } from "@/lib/types/Booking/Revenue/HotelRevenueResponse"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Skeleton } from "@/components/ui/skeleton"

const HotelRevenue = function({ hotelId }: { hotelId: number }) {
  const [hotelRevenue, setHotelRevenue] = useState<HotelRevenueResponse | null>(null)
  const [isLoading, setIsLoading] = useState<boolean>(true)

  useEffect(() => {
    const fetchBooking = async () => {
      setIsLoading(true)
      let data: HotelRevenueResponse | null = null
      try {
        data = await getHotelRevenue(hotelId)
      } catch (error) {
        console.error(error)
      } finally {
        setHotelRevenue(data)
        setIsLoading(false)
      }
    }
    fetchBooking()
  }, [hotelId]) // Added hotelId to dependency array

  if (isLoading) {
    return (
      <Card className="w-full">
        <CardHeader>
          <Skeleton className="h-8 w-64" />
        </CardHeader>
        <CardContent>
          <Skeleton className="h-48 w-full" />
        </CardContent>
      </Card>
    )
  }

  if (!hotelRevenue) {
    return (
      <Card className="w-full">
        <CardContent className="flex items-center justify-center h-48">
          <p className="text-muted-foreground">매출 통계를 불러올 수 없습니다.</p>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle className="text-2xl font-bold text-center">
          총 매출: {hotelRevenue.revenue.toLocaleString()}원
        </CardTitle>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-24">객실 ID</TableHead>
              <TableHead>객실 유형</TableHead>
              <TableHead className="text-right">기본 가격</TableHead>
              <TableHead className="text-right">객실 매출</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {hotelRevenue.roomRevenueResponse.map((room) => (
              <TableRow key={room.roomId}>
                <TableCell>{room.roomId}</TableCell>
                <TableCell>{room.roomName}</TableCell>
                <TableCell className="text-right">
                  {room.basePrice.toLocaleString()}원
                </TableCell>
                <TableCell className="text-right">
                  {room.roomRevenue.toLocaleString()}원
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  )
}

export default HotelRevenue