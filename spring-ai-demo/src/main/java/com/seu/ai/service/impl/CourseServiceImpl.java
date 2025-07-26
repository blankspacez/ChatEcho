package com.seu.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.ai.entity.po.Course;
import com.seu.ai.service.CourseService;
import com.seu.ai.mapper.CourseMapper;
import org.springframework.stereotype.Service;

/**
* @author yuzihao
* @description 针对表【course(学科表)】的数据库操作Service实现
* @createDate 2025-05-29 15:00:14
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
    implements CourseService{

}




