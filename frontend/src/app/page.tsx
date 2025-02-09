'use client';

import Navigation from '@/components/Navigation/Navigation'
import SearchComponent from '@/components/Search/SearchComponent'

export default function Page() {
  return (
    <>
      <Navigation/>
      <div className="min-h-screen bg-background pt-20">
        <div className="container mx-auto px-4 py-8">
          <div className="max-w-4xl mx-auto">
            <SearchComponent />
          </div>
        </div>
      </div>
    </>
  );
}
