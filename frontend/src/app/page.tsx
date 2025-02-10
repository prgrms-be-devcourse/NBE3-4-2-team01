"use client";

import Navigation from "@/components/navigation/Navigation";
import SearchComponent from "@/components/Search/SearchComponent";

export default function Page() {
  return (
    <>
      <Navigation />
      <div className="min-h-screen bg-background">
        <div
          className="container mx-auto px-4 flex items-center justify-center"
          style={{ minHeight: "70vh" }}
        >
          <div className="w-full max-w-[80rem] scale-125">
            <SearchComponent />
          </div>
        </div>
      </div>
    </>
  );
}
