"use client";

import {
  addRoomOption,
  deleteRoomOption,
  getAllRoomOptions,
  modifyRoomOption,
} from "@/lib/api/Admin/RoomOptionApi";
import { OptionResponse } from "@/lib/types/admin/response/OptionResponse";
import { useEffect, useState } from "react";

export default function RoomOptionsPage() {
  const [options, setOptions] = useState<OptionResponse[]>([]);
  const [editingOptions, setEditingOptions] = useState<{
    [key: number]: string;
  }>({});
  const [newOption, setNewOption] = useState<string>("");

  useEffect(() => {
    fetchOptions();
  }, []);

  const fetchOptions = async () => {
    try {
      const data = await getAllRoomOptions();
      setOptions(data);
      setEditingOptions({});
    } catch (error) {
      console.error("객실 옵션을 불러오는 중 오류 발생:", error);
    }
  };

  const onModify = async (id: number) => {
    if (!editingOptions[id]) return;

    try {
      await modifyRoomOption(id, { name: editingOptions[id] });

      setOptions((prevOptions) =>
        prevOptions.map((option) =>
          option.optionId === id
            ? { ...option, name: editingOptions[id] }
            : option
        )
      );

      setEditingOptions((prev) => {
        const updatedEditingOptions = { ...prev };
        delete updatedEditingOptions[id];
        return updatedEditingOptions;
      });
    } catch (error) {
      console.error("객실 옵션 수정 중 오류 발생:", error);
    }
  };

  const onAddOption = async () => {
    if (!newOption.trim()) return;

    try {
      await addRoomOption({ name: newOption });
      setNewOption("");
      fetchOptions();
    } catch (error) {
      console.error("객실 옵션 추가 중 오류 발생:", error);
    }
  };

  return (
    <div>
      <h1>객실 옵션 관리</h1>

      {/* 옵션 추가 기능 */}
      <div>
        <input
          type="text"
          placeholder="새 옵션 입력"
          value={newOption}
          onChange={(e) => setNewOption(e.target.value)}
        />
        <button onClick={onAddOption}>추가</button>
      </div>

      {/* 옵션 목록 */}
      <div>
        {options.map((option, index) => (
          <div key={option.optionId ?? `option-${index}`}>
            {editingOptions[option.optionId] !== undefined ? (
              <input
                value={editingOptions[option.optionId]}
                onChange={(e) =>
                  setEditingOptions({
                    ...editingOptions,
                    [option.optionId]: e.target.value,
                  })
                }
              />
            ) : (
              <span>{option.name}</span>
            )}
            {editingOptions[option.optionId] !== undefined ? (
              <button onClick={() => onModify(option.optionId)}>저장</button>
            ) : (
              <button
                onClick={() =>
                  setEditingOptions({
                    ...editingOptions,
                    [option.optionId]: option.name,
                  })
                }
              >
                수정
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
