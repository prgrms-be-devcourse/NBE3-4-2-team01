'use client'

import { useState, Suspense } from 'react';
import { useRouter } from 'next/navigation';
import HotelList from '@/components/hotellist/HotelList';
import Loading from '@/components/hotellist/Loading';
import Pagination from '@/components/pagination/Pagination';
import Navigation from '@/components/navigation/Navigation';
import { FilterName } from '@/lib/enum/FilterName';
import { Select, SelectTrigger, SelectValue, SelectContent, SelectItem } from '@/components/ui/select';
import { PageDto } from '@/lib/types/PageDto';
import { GetHotelResponse } from '@/lib/types/hotel/GetHotelResponse';

interface HotelsPageClientProps {
  hotelData: PageDto<GetHotelResponse>;
  searchParams: {
    page?: number;
    pageSize?: number;
    filterName?: FilterName;
    streetAddress?: string;
    checkInDate?: string;
    checkoutDate?: string;
    personal?: string;
    filterDirection?: string;
  }
}

export default function HotelsPageClient({ hotelData, searchParams }: HotelsPageClientProps) {
  const router = useRouter();

  // 클라이언트에서 searchParams를 상태로 관리
  const {
    page = 1,
    filterName = FilterName.LATEST
  } = searchParams;

  const [selectedFilter, setSelectedFilter] = useState<FilterName>(filterName);

  const handleFilterChange = (value: FilterName) => {
    setSelectedFilter(value);
    router.push(`?filterName=${value}`);
    console.log(value);
  };

  return (
    <main className="container max-w-6xl mx-auto px-4 py-8">
      <Navigation />
      <div className="content-wrapper flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold mb-6">호텔 목록</h1>
        <div className="ml-auto flex items-center space-x-4"> {/* 가로로 배치되도록 flex와 spacing 적용 */}
          <Select value={selectedFilter} onValueChange={handleFilterChange}>
            <SelectTrigger className="px-4 py-2 border rounded-md bg-white">
              <SelectValue placeholder="정렬 기준" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value={FilterName.LATEST}>최신순</SelectItem>
              <SelectItem value={FilterName.AVERAGE_RATING}>리뷰 점수순</SelectItem>
              <SelectItem value={FilterName.REVIEW_COUNT}>리뷰 개수순</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>
      <Suspense fallback={<Loading />}>
        <HotelList hotels={hotelData.items} />
      </Suspense>
      <Pagination currentPage={page} totalPages={hotelData?.totalPages || 1} basePath="hotels" />
    </main>
  );
}
