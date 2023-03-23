package ua.com.foxminded.asharov.universityschedule.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@Component
public class Adapter {
    public static final int START_POINT_OF_PAGINATION = 1;
    static final int STANDART_LAST_LECTURE = 6;
    static final int STANDART_FIRST_LECTURE = 1;

    private final LectureService lectureServ;
    private final GroupService groupServ;
    private final TeacherService teacherServ;
    private final RoomService roomServ;

    
    public Adapter(LectureService lectureServ, GroupService groupServ, TeacherService teacherServ, RoomService roomServ) {
        this.lectureServ = lectureServ;
        this.groupServ = groupServ;
        this.teacherServ = teacherServ;
        this.roomServ = roomServ;
    }

    public SortedMap<LocalDate, ArrayList<Lecture>> stuffTimetableByOwnername(LocalDate sDate, LocalDate fDate,
            Long ownerId, String ownername) {

        if (ownername.equals("group")) {
            return stuffTimetable(sDate, fDate, lectureServ.retrieveGroupLectures(ownerId, sDate, fDate));
        }

        if (ownername.equals("teacher")) {
            return stuffTimetable(sDate, fDate, lectureServ.retrieveTeacherLectures(ownerId, sDate, fDate));
        }

        if (ownername.equals("room")) {
            return stuffTimetable(sDate, fDate, lectureServ.retrieveRoomLectures(ownerId, sDate, fDate));
        }
        return stuffTimetable(sDate, fDate, new ArrayList<>(Arrays.asList(new Lecture())));
    }

    public List<SortedMap<LocalDate, ArrayList<Lecture>>> wrapForMonthView(
            SortedMap<LocalDate, ArrayList<Lecture>> timetablePerDay) {

        List<SortedMap<LocalDate, ArrayList<Lecture>>> timetablePerMonth = new ArrayList<>();
        timetablePerDay = fillGapsWithBlankLecture(fillGapsWithBlankDay(getFirstDayOfMonth(timetablePerDay.firstKey()),
                getLastDayOfMonth(timetablePerDay.lastKey()), timetablePerDay));

        for (LocalDate forMonth = timetablePerDay.firstKey(); forMonth
                .isBefore(timetablePerDay.lastKey()); forMonth = forMonth.plusMonths(1)) {
            SortedMap<LocalDate, ArrayList<Lecture>> timetableWithinMonth = new TreeMap<>();

            for (LocalDate forDay = forMonth; forDay.isBefore(forMonth.plusMonths(1)); forDay = forDay.plusDays(1)) {
                timetableWithinMonth.put(forDay, timetablePerDay.get(forDay));
            }
            timetablePerMonth.add(timetableWithinMonth);
        }
        return timetablePerMonth;
    }

    private LocalDate getLastDayOfMonth(LocalDate fDate) {
        return LocalDate.of(fDate.plusMonths(1).getYear(), fDate.plusMonths(1).getMonthValue(), 1).minusDays(1);
    }

    private LocalDate getFirstDayOfMonth(LocalDate sDate) {
        return LocalDate.of(sDate.getYear(), sDate.getMonthValue(), 1);
    }

    public SortedMap<LocalDate, ArrayList<Lecture>> stuffTimetable(LocalDate sDate, LocalDate fDate,
            List<Lecture> list) {
        return fillGapsWithBlankLecture(fillGapsWithBlankDay(sDate, fDate,
                list.stream().sorted((a1, a2) -> a1.getSerialNumberPerDay().compareTo(a2.getSerialNumberPerDay()))
                        .collect(Collectors.groupingBy(Lecture::getDate, TreeMap::new,
                                Collectors.toCollection(ArrayList::new)))));
    }

    public SortedMap<LocalDate, ArrayList<Lecture>> fillGapsWithBlankLecture(
            SortedMap<LocalDate, ArrayList<Lecture>> timtable) {
        timtable.entrySet().forEach(entry -> {
            List<Integer> num = entry.getValue().stream().map(Lecture::getSerialNumberPerDay).sorted()
                    .collect(Collectors.toList());

            for (int i = Math.min(STANDART_FIRST_LECTURE, num.get(0)); i <= Math.max(STANDART_LAST_LECTURE,
                    num.get(num.size() - 1)); i++) {
                if (!num.contains(i)) {
                    entry.getValue().add(new Lecture(i, entry.getKey()));
                }
            }
            entry.getValue().sort((l1, l2) -> l1.getSerialNumberPerDay().compareTo(l2.getSerialNumberPerDay()));
        });
        return timtable;
    }

    public SortedMap<LocalDate, ArrayList<Lecture>> fillGapsWithBlankDay(LocalDate sDate, LocalDate fDate,
            SortedMap<LocalDate, ArrayList<Lecture>> realLecturesDays) {
        return Stream.iterate(sDate, x -> x.isBefore(fDate.plusDays(1)), x -> x.plusDays(1))
                .map(d -> realLecturesDays.getOrDefault(d, new ArrayList<>(Arrays.asList(new Lecture(1, d)))))
                .collect(Collectors.toMap(x -> x.get(0).getDate(), x -> x, (x, y) -> x, TreeMap::new));
    }
  
    public List<List<LocalDate>> stretchMonthTimeline(LocalDate sDate, LocalDate fDate) {
        List<List<LocalDate>> result = new ArrayList<>();

        for (LocalDate forMonth = getFirstDayOfMonth(sDate); forMonth
                .isBefore(getLastDayOfMonth(fDate)); forMonth = forMonth.plusMonths(1)) {
            List<LocalDate> month = new ArrayList<>();

            for (LocalDate forDay = forMonth; forDay.isBefore(forMonth.plusMonths(1)); forDay = forDay.plusDays(1)) {
                month.add(forDay);
            }
            result.add(month);
        }
        return result;
    }

    public SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> getTimetable(Long groupId, Long teacherId,
            Long roomId, LocalDate sDate, LocalDate fDate) {
        return fillLecturesGapByBlank(fillDayGapByBlank(lectureServ.retrieveAllByGroupIdByTeacherIdByRoomId(groupId, teacherId, roomId, sDate, fDate), sDate, fDate));
    }

    public SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> fillDayGapByBlank(
            SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> timetable, LocalDate sDate, LocalDate fDate) {

        for (LocalDate day = sDate; day.isBefore(fDate); day = day.plusDays(1)) {
            SortedMap<Integer, List<Lecture>> blankDay = new TreeMap<>();
            blankDay.put(1, new ArrayList<>(Arrays.asList(new Lecture(1, day))));
            timetable.putIfAbsent(day, blankDay);
        }
        return timetable;
    }

    public SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> fillLecturesGapByBlank(
            SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> timtable) {

        timtable.entrySet().forEach(entry -> {
            for (int i = STANDART_FIRST_LECTURE; i <= STANDART_LAST_LECTURE; i++) {
                entry.getValue().putIfAbsent(i, Arrays.asList(new Lecture(i, entry.getKey())));
            }
        });
        return timtable;
    }

    @SuppressWarnings("rawtypes")
    public Optional getOwnerByIdByName(Long ownerId, String ownername) {
        Optional result = Optional.empty();
        
        if (ownername != null && ownername.equals("student")) {
            result = Optional.of(groupServ.retrieveGroupByStudentId(ownerId));
        }
        
        if (ownername != null && ownername.equals("group")) {
            result = Optional.of(groupServ.retrieveById(ownerId));
        }

        if (ownername != null && ownername.equals("teacher")) {
            result = Optional.of(teacherServ.retrieveById(ownerId));
        }

        if (ownername != null && ownername.equals("room")) {
            result = Optional.of(roomServ.retrieveById(ownerId));
        }
        return result;
    }
    
    public int getPage(String sDate, LocalDate currentDate) {
        return Period.between(LocalDate.parse(sDate), currentDate).getDays()+START_POINT_OF_PAGINATION;
    }
 
}
