"use client";

import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import { useState } from "react";

interface HotelImagesProps {
  images: string[];
}

const HotelImages: React.FC<HotelImagesProps> = ({ images }) => {
  const [selectedImage, setSelectedImage] = useState<string | null>(null);

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
                className="w-full h-auto max-h-60 object-contain rounded-lg cursor-pointer transition-transform duration-200 hover:scale-105"
                onClick={() => setSelectedImage(src)}
              />
            </SwiperSlide>
          ))}
        </Swiper>
      </div>

      {/* 모달 - 이미지 확대 */}
      {selectedImage && (
        <div
          className="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50"
          onClick={() => setSelectedImage(null)}
        >
          <div className="relative p-4 bg-white rounded-lg shadow-lg max-w-3xl">
            <button
              className="absolute top-2 right-2 text-gray-600 hover:text-gray-900 test-2x1"
              onClick={() => setSelectedImage(null)}
            >
              X
            </button>
            <img
              src={selectedImage}
              alt="확대한 이미지"
              className="w-full max-h-[80vh] object-contain rounded-lg"
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default HotelImages;
