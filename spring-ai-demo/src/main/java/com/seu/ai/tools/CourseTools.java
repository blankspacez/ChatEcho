package com.seu.ai.tools;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.seu.ai.entity.po.Course;
import com.seu.ai.entity.po.CourseReservation;
import com.seu.ai.entity.po.School;
import com.seu.ai.entity.query.CourseQuery;
import com.seu.ai.service.CourseReservationService;
import com.seu.ai.service.CourseService;
import com.seu.ai.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseTools {

    private final CourseService courseService;

    private final CourseReservationService courseReservationService;

    private final SchoolService schoolService;

    @Tool(description = "根据条件进行课程查询")
    public List<Course> queryCourse(@ToolParam(description = "查询的条件") CourseQuery query){
        if(query == null){
            return courseService.list();
        }
        QueryChainWrapper<Course> wrapper = courseService.query()
                .eq(query.getType() != null, "type", query.getType())
                .le(query.getEdu() != null, "edu", query.getEdu());
        if (query.getSorts() != null && !query.getSorts().isEmpty()) {
            for (CourseQuery.Sort sort : query.getSorts()) {
                wrapper.orderBy(true,sort.getAsc(), sort.getField());
            }
        }
        return wrapper.list();
    }

    @Tool(description = "根据条件生成课程预约单号")
    public Integer createCourseReservation(@ToolParam(description = "预约课程") String course,
                                           @ToolParam(description = "预约校区") String school,
                                           @ToolParam(description = "预约人姓名") String studentName,
                                           @ToolParam(description = "预约人电话") String contactInfo,
                                           @ToolParam(required = false, description = "备注") String remark){
        CourseReservation reservation = new CourseReservation();
        reservation.setCourse(course);
        reservation.setSchool(school);
        reservation.setStudentName(studentName);
        reservation.setContactInfo(contactInfo);
        reservation.setRemark(remark);
        return courseReservationService.save(reservation) ? reservation.getId() : null;
    }

    @Tool(description = "查询校区列表")
    public List<String> getSchoolList(){
        return schoolService.list().stream().map(School::getName).toList();
    }
}
