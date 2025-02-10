'use client';

import Navigation from '@/components/navigation/Navigation';
import SearchComponent from '@/components/Search/SearchComponent';
import { Building2, MapPin, Star, Waves } from 'lucide-react';

export default function Page() {
  return (
    <div className="relative min-h-screen bg-background">
      {/* Background gradient */}
      <div className="absolute inset-0 bg-gradient-to-b from-blue-100 to-white" />
      
      {/* Decorative circles */}
      <div className="absolute top-20 right-20 w-64 h-64 bg-blue-200 rounded-full blur-3xl opacity-20" />
      <div className="absolute bottom-20 left-20 w-96 h-96 bg-blue-300 rounded-full blur-3xl opacity-10" />
      
      <div className="relative z-10">
        <Navigation />
        
        <div className="content-wrapper container mx-auto px-4">
          {/* Hero Section */}
          <div className="text-center mt-20 mb-12">
            <h1 className="text-4xl font-bold text-gray-800 mb-4">
              서울에서 완벽한 호텔을 찾아보세요
            </h1>
            <p className="text-lg text-gray-600 mb-8">
              최고의 위치, 최상의 서비스로 특별한 경험을 제공합니다
            </p>
          </div>

          {/* Search Component */}
          <div className="relative w-full max-w-[80rem] mx-auto scale-125">
            <SearchComponent />
          </div>

          {/* Features Section */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-32 mb-16">
            {[
              { icon: Building2, title: '다양한 호텔', desc: '1성급부터 5성급까지 다양한 등급' },
              { icon: MapPin, title: '최적의 위치', desc: '서울 곳곳의 편리한 위치' },
              { icon: Star, title: '검증된 평점', desc: '실제 이용객의 솔직한 리뷰' },
            ].map((feature, idx) => (
              <div
                key={idx}
                className="flex flex-col items-center text-center p-6 bg-white/50 rounded-lg shadow-sm"
              >
                <feature.icon className="w-12 h-12 text-blue-500 mb-4" />
                <h3 className="text-lg font-semibold text-gray-800 mb-2">{feature.title}</h3>
                <p className="text-gray-600">{feature.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}