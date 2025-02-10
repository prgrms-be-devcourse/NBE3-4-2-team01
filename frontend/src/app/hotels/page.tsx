import { getHotelList } from '@/lib/api/AwsS3Api';
import { PageDto } from '@/lib/types/PageDto';
import { GetHotelResponse } from '@/lib/types/hotel/GetHotelResponse';
import { FilterName } from '@/lib/enum/FilterName';
import { SeoulDistrict } from '@/lib/enum/SeoulDistriction';
import { FilterDirection } from '@/lib/enum/FilterDirection';
import HotelsPageClient from './HotelsPage.client';

interface PageProps {
  searchParams: {
    page?: number;
    pageSize?: number;
    filterName?: FilterName;
    streetAddress?: SeoulDistrict;
    checkInDate?: string;
    checkoutDate?: string;
    personal?: string;
    filterDirection?: FilterDirection;
  }
}

export default async function HotelsPage({ searchParams }: PageProps) {
  const params = await Promise.resolve(searchParams);
  
  const {
    page = 1,
    pageSize = 10,
    filterName = FilterName.LATEST,
    streetAddress = SeoulDistrict.DEFAULT,
    checkInDate = new Date().toISOString().split('T')[0],
    checkoutDate = new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split('T')[0],
    personal = '2',
    filterDirection = FilterDirection.DESC
  } = params;

  const hotelData: PageDto<GetHotelResponse> = await getHotelList(
    page,
    pageSize,
    filterName,
    streetAddress,
    checkInDate,
    checkoutDate,
    parseInt(personal),
    filterDirection
  );

  return <HotelsPageClient hotelData={hotelData} searchParams={params} />;
}
