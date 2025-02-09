"use client";

import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";

interface HotelImagesProps {
  images: string[];
}

const HotelImages: React.FC<HotelImagesProps> = ({ images }) => {
  if (images.length === 0) {
    return <p>등록된 호텔 이미지가 없습니다.</p>;
  }

  return (
    <div className="container mx-auto px-6">
      <div className="mb-12">
        <h2 className="text-2xl font-bold mb-5">호텔 이미지</h2>
        <Swiper
          modules={[Navigation]}
          spaceBetween={15}
          slidesPerView={3}
          navigation
          loop={true}
          className="rouded-lg shadow-lg"
        >
          {images.map((src, index) => (
            <SwiperSlide key={index}>
              <img
                src={src}
                alt={`호텔 이미지 ${index + 1}`}
                className="w-full h-52 object-cover rounded-lg"
              />
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
    </div>
  );
};

export default HotelImages;
